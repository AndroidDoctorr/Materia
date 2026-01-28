package com.torr.materia.block;

/**
 * AND Gate Block - outputs true only when both inputs A and B are true
 */
public class AndGateBlock extends LogicGateBlock {
    
    public AndGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return inputA && inputB;
    }
}
