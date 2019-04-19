/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BlockChainTestModule } from '../../../test.module';
import { OutputDeleteDialogComponent } from 'app/entities/output/output-delete-dialog.component';
import { OutputService } from 'app/entities/output/output.service';

describe('Component Tests', () => {
    describe('Output Management Delete Component', () => {
        let comp: OutputDeleteDialogComponent;
        let fixture: ComponentFixture<OutputDeleteDialogComponent>;
        let service: OutputService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [OutputDeleteDialogComponent]
            })
                .overrideTemplate(OutputDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OutputDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutputService);
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
