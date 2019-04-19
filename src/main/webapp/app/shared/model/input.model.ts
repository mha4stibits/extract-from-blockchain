export interface IInput {
    id?: number;
    sequence?: number;
    scriptSignature?: string;
    transactionId?: number;
    previousOutputId?: number;
}

export class Input implements IInput {
    constructor(
        public id?: number,
        public sequence?: number,
        public scriptSignature?: string,
        public transactionId?: number,
        public previousOutputId?: number
    ) {}
}
