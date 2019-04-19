import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInput } from 'app/shared/model/input.model';
import { InputService } from './input.service';

@Component({
    selector: 'jhi-input-delete-dialog',
    templateUrl: './input-delete-dialog.component.html'
})
export class InputDeleteDialogComponent {
    input: IInput;

    constructor(protected inputService: InputService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.inputService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'inputListModification',
                content: 'Deleted an input'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-input-delete-popup',
    template: ''
})
export class InputDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ input }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InputDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.input = input;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/input', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/input', { outlets: { popup: null } }]);
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
