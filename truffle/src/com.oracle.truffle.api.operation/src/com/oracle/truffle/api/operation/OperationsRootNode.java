package com.oracle.truffle.api.operation;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleStackTraceElement;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class OperationsRootNode extends RootNode {

    @Child private OperationsNode node;

    private final String nodeName;
    private final boolean isInternal;

    OperationsRootNode(TruffleLanguage<?> language, OperationsNode node, String nodeName, boolean isInternal) {
        super(language, node.createFrameDescriptor());
        this.node = insert(node);
        this.nodeName = nodeName;
        this.isInternal = isInternal;
    }

    @Override
    public String getName() {
        return nodeName;
    }

    @Override
    public boolean isInternal() {
        return isInternal;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return node.execute(frame);
    }

    @Override
    public boolean isCaptureFramesForTrace() {
        return true;
    }

    @Override
    protected Object translateStackTraceElement(TruffleStackTraceElement element) {
        int bci = element.getFrame().getInt(OperationsNode.BCI_SLOT);
        return new OperationsStackTraceElement(element.getTarget().getRootNode(), node.getSourceSectionAtBci(bci));
    }

    @Override
    public boolean isInstrumentable() {
        return false;
    }

// private class OperationsWrapperNode extends Node implements WrapperNode {
// private final ProbeNode probe;
//
// OperationsWrapperNode(ProbeNode probe) {
// this.probe = probe;
// }
//
// public Node getDelegateNode() {
// return OperationsRootNode.this;
// }
//
// public ProbeNode getProbeNode() {
// return probe;
// }
// }

// public WrapperNode createWrapper(final ProbeNode probe) {
// // return new OperationsWrapperNode(probe);
// return null;
// }

    @Override
    public String toString() {
        return "root " + getName();
    }

}
