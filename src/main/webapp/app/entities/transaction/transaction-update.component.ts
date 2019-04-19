import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { IBlock } from 'app/shared/model/block.model';
import { BlockService } from 'app/entities/block';

@Component({
    selector: 'jhi-transaction-update',
    templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
    transaction: ITransaction;
    isSaving: boolean;

    blocks: IBlock[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected transactionService: TransactionService,
        protected blockService: BlockService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ transaction }) => {
            this.transaction = transaction;
        });
        this.blockService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IBlock[]>) => mayBeOk.ok),
                map((response: HttpResponse<IBlock[]>) => response.body)
            )
            .subscribe((res: IBlock[]) => (this.blocks = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.transaction.id !== undefined) {
            this.subscribeToSaveResponse(this.transactionService.update(this.transaction));
        } else {
            this.subscribeToSaveResponse(this.transactionService.create(this.transaction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>) {
        result.subscribe((res: HttpResponse<ITransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBlockById(index: number, item: IBlock) {
        return item.id;
    }
}
