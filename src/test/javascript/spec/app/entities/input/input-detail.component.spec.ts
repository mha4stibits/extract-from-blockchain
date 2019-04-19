/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { InputDetailComponent } from 'app/entities/input/input-detail.component';
import { Input } from 'app/shared/model/input.model';

describe('Component Tests', () => {
    describe('Input Management Detail Component', () => {
        let comp: InputDetailComponent;
        let fixture: ComponentFixture<InputDetailComponent>;
        const route = ({ data: of({ input: new Input(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [InputDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InputDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InputDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.input).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
