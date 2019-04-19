/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlockChainTestModule } from '../../../test.module';
import { InputComponent } from 'app/entities/input/input.component';
import { InputService } from 'app/entities/input/input.service';
import { Input } from 'app/shared/model/input.model';

describe('Component Tests', () => {
    describe('Input Management Component', () => {
        let comp: InputComponent;
        let fixture: ComponentFixture<InputComponent>;
        let service: InputService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [InputComponent],
                providers: []
            })
                .overrideTemplate(InputComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InputComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InputService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Input(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.inputs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
