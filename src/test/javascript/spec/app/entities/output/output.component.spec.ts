/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlockChainTestModule } from '../../../test.module';
import { OutputComponent } from 'app/entities/output/output.component';
import { OutputService } from 'app/entities/output/output.service';
import { Output } from 'app/shared/model/output.model';

describe('Component Tests', () => {
    describe('Output Management Component', () => {
        let comp: OutputComponent;
        let fixture: ComponentFixture<OutputComponent>;
        let service: OutputService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [OutputComponent],
                providers: []
            })
                .overrideTemplate(OutputComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OutputComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutputService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Output(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.outputs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
