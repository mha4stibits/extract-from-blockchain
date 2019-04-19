/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { InputUpdateComponent } from 'app/entities/input/input-update.component';
import { InputService } from 'app/entities/input/input.service';
import { Input } from 'app/shared/model/input.model';

describe('Component Tests', () => {
    describe('Input Management Update Component', () => {
        let comp: InputUpdateComponent;
        let fixture: ComponentFixture<InputUpdateComponent>;
        let service: InputService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [InputUpdateComponent]
            })
                .overrideTemplate(InputUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InputUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InputService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Input(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.input = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Input();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.input = entity;
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
