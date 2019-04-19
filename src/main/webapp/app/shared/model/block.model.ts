import { Moment } from 'moment';
import { ITransaction } from 'app/shared/model/transaction.model';

export interface IBlock {
    id?: number;
    version?: number;
    previousBlockHash?: string;
    merkleRoot?: string;
    bits?: number;
    fees?: number;
    nonce?: number;
    size?: number;
    index?: number;
    receivedTime?: number;
    relayedBy?: string;
    height?: number;
    hash?: string;
    time?: Moment;
    mainChain?: boolean;
    transactions?: ITransaction[];
}

export class Block implements IBlock {
    constructor(
        public id?: number,
        public version?: number,
        public previousBlockHash?: string,
        public merkleRoot?: string,
        public bits?: number,
        public fees?: number,
        public nonce?: number,
        public size?: number,
        public index?: number,
        public receivedTime?: number,
        public relayedBy?: string,
        public height?: number,
        public hash?: string,
        public time?: Moment,
        public mainChain?: boolean,
        public transactions?: ITransaction[]
    ) {
        this.mainChain = this.mainChain || false;
    }
}
