import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBlock } from 'app/shared/model/block.model';
import { AccountService } from 'app/core';
import { BlockService } from './block.service';

@Component({
    selector: 'jhi-block',
    templateUrl: './block.component.html'
})
export class BlockComponent implements OnInit, OnDestroy {
    blocks: IBlock[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected blockService: BlockService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.blockService
            .query()
            .pipe(
                filter((res: HttpResponse<IBlock[]>) => res.ok),
                map((res: HttpResponse<IBlock[]>) => res.body)
            )
            .subscribe(
                (res: IBlock[]) => {
                    this.blocks = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBlocks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBlock) {
        return item.id;
    }

    registerChangeInBlocks() {
        this.eventSubscriber = this.eventManager.subscribe('blockListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
