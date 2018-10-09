package com.example.lambda.enum_;

/**
 * 枚举操作类
 * Created by lee on 05/27/18.
 */
public enum OperationEnum {
    /**
     * 加法操作
     */
    ADD {
        @Override
        public int eval(int a, int b) {return a + b;}
    },
    SUBTRACT {
        @Override
        public int eval(int a, int b) {return a - b;}
    },
    MULTIPLY {
        @Override
        public int eval(int a, int b) {return a * b;}
    },
    DIVIDE {
        @Override
        public int eval(int a, int b) {return a / b;}
    };

    public abstract int eval(int a, int b);
}
