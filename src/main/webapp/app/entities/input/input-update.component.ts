import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IInput } from 'app/shared/model/input.model';
import { InputService } from './input.service';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from 'app/entities/transaction';
import { IOutput } from 'app/shared/model/output.model';
import { OutputService } from 'app/entities/output';

@Component({
    selector: 'jhi-input-update',
    templateUrl: './input-update.component.html'
})
export class InputUpdateComponent implements OnInit {
    input: IInput;
    isSaving: boolean;

    transactions: ITransaction[];

    previousoutputs: IOutput[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected inputService: InputService,
        protected transactionService: TransactionService,
        protected outputService: OutputService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ input }) => {
            this.input = input;
        });
        this.transactionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITransaction[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITransaction[]>) => response.body)
            )
            .subscribe((res: ITransaction[]) => (this.transactions = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.outputService
            .query({ filter: 'input-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IOutput[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOutput[]>) => response.body)
            )
            .subscribe(
                (res: IOutput[]) => {
                    if (!this.input.previousOutputId) {
                        this.previousoutputs = res;
                    } else {
                        this.outputService
                            .find(this.input.previousOutputId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IOutput>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IOutput>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IOutput) => (this.previousoutputs = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.input.id !== undefined) {
            this.subscribeToSaveResponse(this.inputService.update(this.input));
        } else {
            this.subscribeToSaveResponse(this.inputService.create(this.input));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IInput>>) {
        result.subscribe((res: HttpResponse<IInput>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOutputById(index: number, item: IOutput) {
        return item.id;
    }
}
