package com.torr.materia.block;

/**
 * OR Gate Block - outputs true when at least one input (A or B) is true
 */
public class OrGateBlock extends LogicGateBlock {
    
    public OrGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return inputA || inputB;
    }
}
