/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BlockChainTestModule } from '../../../test.module';
import { BlockDeleteDialogComponent } from 'app/entities/block/block-delete-dialog.component';
import { BlockService } from 'app/entities/block/block.service';

describe('Component Tests', () => {
    describe('Block Management Delete Component', () => {
        let comp: BlockDeleteDialogComponent;
        let fixture: ComponentFixture<BlockDeleteDialogComponent>;
        let service: BlockService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [BlockDeleteDialogComponent]
            })
                .overrideTemplate(BlockDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BlockDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BlockService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
