/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { OutputUpdateComponent } from 'app/entities/output/output-update.component';
import { OutputService } from 'app/entities/output/output.service';
import { Output } from 'app/shared/model/output.model';

describe('Component Tests', () => {
    describe('Output Management Update Component', () => {
        let comp: OutputUpdateComponent;
        let fixture: ComponentFixture<OutputUpdateComponent>;
        let service: OutputService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [OutputUpdateComponent]
            })
                .overrideTemplate(OutputUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OutputUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutputService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Output(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.output = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Output();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.output = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
