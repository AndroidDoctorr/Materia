package com.torr.materia.block;

/**
 * NAND Gate Block - outputs true when at least one input (A or B) is false
 */
public class NandGateBlock extends LogicGateBlock {
    
    public NandGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return !(inputA && inputB);
    }
}
