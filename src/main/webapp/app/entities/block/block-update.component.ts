import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IBlock } from 'app/shared/model/block.model';
import { BlockService } from './block.service';

@Component({
    selector: 'jhi-block-update',
    templateUrl: './block-update.component.html'
})
export class BlockUpdateComponent implements OnInit {
    block: IBlock;
    isSaving: boolean;
    time: string;

    constructor(protected blockService: BlockService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ block }) => {
            this.block = block;
            this.time = this.block.time != null ? this.block.time.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.block.time = this.time != null ? moment(this.time, DATE_TIME_FORMAT) : null;
        if (this.block.id !== undefined) {
            this.subscribeToSaveResponse(this.blockService.update(this.block));
        } else {
            this.subscribeToSaveResponse(this.blockService.create(this.block));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlock>>) {
        result.subscribe((res: HttpResponse<IBlock>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
