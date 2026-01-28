package com.torr.materia.block;

/**
 * XNOR Gate Block - outputs true when inputs A and B are the same
 */
public class XnorGateBlock extends LogicGateBlock {
    
    public XnorGateBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected boolean computeOutput(boolean inputA, boolean inputB) {
        return inputA == inputB;
    }
}
