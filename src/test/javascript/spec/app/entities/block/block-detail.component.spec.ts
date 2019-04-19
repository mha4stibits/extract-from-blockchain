/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { BlockDetailComponent } from 'app/entities/block/block-detail.component';
import { Block } from 'app/shared/model/block.model';

describe('Component Tests', () => {
    describe('Block Management Detail Component', () => {
        let comp: BlockDetailComponent;
        let fixture: ComponentFixture<BlockDetailComponent>;
        const route = ({ data: of({ block: new Block(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [BlockDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BlockDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BlockDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.block).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
