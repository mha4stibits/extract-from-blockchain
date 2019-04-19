import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBlock } from 'app/shared/model/block.model';
import { BlockService } from './block.service';

@Component({
    selector: 'jhi-block-delete-dialog',
    templateUrl: './block-delete-dialog.component.html'
})
export class BlockDeleteDialogComponent {
    block: IBlock;

    constructor(protected blockService: BlockService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.blockService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'blockListModification',
                content: 'Deleted an block'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-block-delete-popup',
    template: ''
})
export class BlockDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ block }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BlockDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.block = block;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/block', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/block', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
