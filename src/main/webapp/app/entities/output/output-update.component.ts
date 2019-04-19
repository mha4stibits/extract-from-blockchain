import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOutput } from 'app/shared/model/output.model';
import { OutputService } from './output.service';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from 'app/entities/transaction';

@Component({
    selector: 'jhi-output-update',
    templateUrl: './output-update.component.html'
})
export class OutputUpdateComponent implements OnInit {
    output: IOutput;
    isSaving: boolean;

    transactions: ITransaction[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected outputService: OutputService,
        protected transactionService: TransactionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ output }) => {
            this.output = output;
        });
        this.transactionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITransaction[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITransaction[]>) => response.body)
            )
            .subscribe((res: ITransaction[]) => (this.transactions = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.output.id !== undefined) {
            this.subscribeToSaveResponse(this.outputService.update(this.output));
        } else {
            this.subscribeToSaveResponse(this.outputService.create(this.output));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOutput>>) {
        result.subscribe((res: HttpResponse<IOutput>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTransactionById(index: number, item: ITransaction) {
        return item.id;
    }
}
