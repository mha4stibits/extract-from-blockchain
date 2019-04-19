import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IInput } from 'app/shared/model/input.model';
import { AccountService } from 'app/core';
import { InputService } from './input.service';

@Component({
    selector: 'jhi-input',
    templateUrl: './input.component.html'
})
export class InputComponent implements OnInit, OnDestroy {
    inputs: IInput[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected inputService: InputService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.inputService
            .query()
            .pipe(
                filter((res: HttpResponse<IInput[]>) => res.ok),
                map((res: HttpResponse<IInput[]>) => res.body)
            )
            .subscribe(
                (res: IInput[]) => {
                    this.inputs = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInInputs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInput) {
        return item.id;
    }

    registerChangeInInputs() {
        this.eventSubscriber = this.eventManager.subscribe('inputListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
