/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BlockChainTestModule } from '../../../test.module';
import { BlockUpdateComponent } from 'app/entities/block/block-update.component';
import { BlockService } from 'app/entities/block/block.service';
import { Block } from 'app/shared/model/block.model';

describe('Component Tests', () => {
    describe('Block Management Update Component', () => {
        let comp: BlockUpdateComponent;
        let fixture: ComponentFixture<BlockUpdateComponent>;
        let service: BlockService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BlockChainTestModule],
                declarations: [BlockUpdateComponent]
            })
                .overrideTemplate(BlockUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BlockUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BlockService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Block(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.block = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Block();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.block = entity;
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
