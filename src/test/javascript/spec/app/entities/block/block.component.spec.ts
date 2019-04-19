/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlockChainTestModule } from '../../../test.module';
import { BlockComponent } from 'app/entities/block/block.component';
import { BlockService } from 'app/entities/block/block.service';
import { Block } from 'app/shared/model/block.model';

describe('Component Tests', () => {
    describe('Block Management Component', () => {
        let comp: BlockComponent;
        let fixture: ComponentFixture<BlockComponent>;
        let service: BlockService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [BlockComponent],
                providers: []
            })
                .overrideTemplate(BlockComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BlockComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BlockService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Block(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.blocks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
