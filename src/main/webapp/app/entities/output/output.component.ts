import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IOutput } from 'app/shared/model/output.model';
import { AccountService } from 'app/core';
import { OutputService } from './output.service';

@Component({
    selector: 'jhi-output',
    templateUrl: './output.component.html'
})
export class OutputComponent implements OnInit, OnDestroy {
    outputs: IOutput[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected outputService: OutputService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.outputService
            .query()
            .pipe(
                filter((res: HttpResponse<IOutput[]>) => res.ok),
                map((res: HttpResponse<IOutput[]>) => res.body)
            )
            .subscribe(
                (res: IOutput[]) => {
                    this.outputs = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInOutputs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IOutput) {
        return item.id;
    }

    registerChangeInOutputs() {
        this.eventSubscriber = this.eventManager.subscribe('outputListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
