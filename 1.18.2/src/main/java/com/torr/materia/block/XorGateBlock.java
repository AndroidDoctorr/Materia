package com.torr.materia.block;

/**
 * XOR Gate Block - outputs true when inputs A and B are different
 */
public class XorGateBlock extends LogicGateBlock {
    
    public XorGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return inputA != inputB;
    }
}
