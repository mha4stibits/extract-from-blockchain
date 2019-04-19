/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { OutputDetailComponent } from 'app/entities/output/output-detail.component';
import { Output } from 'app/shared/model/output.model';

describe('Component Tests', () => {
    describe('Output Management Detail Component', () => {
        let comp: OutputDetailComponent;
        let fixture: ComponentFixture<OutputDetailComponent>;
        const route = ({ data: of({ output: new Output(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [OutputDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OutputDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OutputDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.output).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
