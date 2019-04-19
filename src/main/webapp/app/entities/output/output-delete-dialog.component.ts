import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOutput } from 'app/shared/model/output.model';
import { OutputService } from './output.service';

@Component({
    selector: 'jhi-output-delete-dialog',
    templateUrl: './output-delete-dialog.component.html'
})
export class OutputDeleteDialogComponent {
    output: IOutput;

    constructor(protected outputService: OutputService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.outputService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'outputListModification',
                content: 'Deleted an output'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-output-delete-popup',
    template: ''
})
export class OutputDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ output }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OutputDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.output = output;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/output', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/output', { outlets: { popup: null } }]);
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
