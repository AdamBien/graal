package com.oracle.truffle.dsl.processor.operations.instructions;

import javax.lang.model.type.TypeMirror;

import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.java.model.CodeTree;
import com.oracle.truffle.dsl.processor.java.model.CodeTreeBuilder;

public class ConditionalBranchInstruction extends Instruction {
    public ConditionalBranchInstruction(int id) {
        super("brfalse", id, ResultType.BRANCH, InputType.BRANCH_TARGET, InputType.STACK_VALUE, InputType.BRANCH_PROFILE);
    }

    @Override
    public TypeMirror[] expectedInputTypes(ProcessorContext context) {
        return new TypeMirror[]{
                        context.getType(short.class),
                        context.getType(boolean.class),
                        context.getDeclaredType("com.oracle.truffle.api.profiles.ConditionProfile")
        };
    }

    @Override
    public CodeTree createExecuteCode(ExecutionVariables vars) {
        CodeTreeBuilder b = CodeTreeBuilder.createBuilder();

        b.startIf().startCall(vars.inputs[2], "profile").variable(vars.inputs[1]).end(2);
        b.startBlock();
        b.startAssign(vars.results[0]).variable(vars.bci).string(" + " + length()).end();
        b.statement("continue loop");
        b.end().startElseBlock();
        b.startAssign(vars.results[0]).variable(vars.inputs[0]).end();
        b.statement("continue loop");
        b.end();

        return b.build();
    }
}