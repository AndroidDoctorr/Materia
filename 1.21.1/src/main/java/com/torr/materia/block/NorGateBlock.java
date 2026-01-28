package com.torr.materia.block;

/**
 * NOR Gate Block - outputs true only when both inputs A and B are false
 */
public class NorGateBlock extends LogicGateBlock {
    
    public NorGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return !(inputA || inputB);
    }
}
