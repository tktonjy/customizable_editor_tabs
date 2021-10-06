package asm;

import org.jetbrains.org.objectweb.asm.AnnotationVisitor;
import org.jetbrains.org.objectweb.asm.ClassVisitor;
import org.jetbrains.org.objectweb.asm.Handle;
import org.jetbrains.org.objectweb.asm.Label;
import org.jetbrains.org.objectweb.asm.MethodVisitor;
import org.jetbrains.org.objectweb.asm.Opcodes;

public class MethodReplacer extends ClassVisitor implements Opcodes {
    private String mname;
    private String cname;

    public MethodReplacer(ClassVisitor cv, String mname) {
        super(Opcodes.ASM7, cv);
        this.mname = mname;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        this.cname = name;
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        String newName = name;

        if (name.equals("setText") && desc.equals("(Ljava/lang/String;)Lcom/intellij/ui/tabs/TabInfo;")) {
            newName = "orig$" + name;
            generateSetTextBody(access, desc, signature, exceptions, name);
            System.out.println("Replacing setText");
        }

//        if (!"orig$setText".equals(name))
//            return super.visitMethod(access, newName, desc, signature, exceptions);
//        else {
//            return super.visitMethod(access, "orig$setText1", desc, signature, exceptions);
//        }

        if (name.equals("getTooltipText") && desc.equals("()Ljava/lang/String;")) {
            newName = "orig$" + name;
            generateGetTooltipBody(access, desc, signature, exceptions, name);
            System.out.println("Replacing getTooltipText");
        }

        return super.visitMethod(access, newName, desc, signature, exceptions);
    }

    private void generateSetTextBody(int access, String desc, String signature, String[] exceptions,
                                     String name) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        {
//            AnnotationVisitor annotationVisitor0;
//            methodVisitor.visitCode();
//            Label label0 = new Label();
//            Label label1 = new Label();
//            Label label2 = new Label();
//            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/NumberFormatException");
//            Label label3 = new Label();
//            methodVisitor.visitLabel(label3);
//            methodVisitor.visitLineNumber(103, label3);
//            methodVisitor.visitIntInsn(BIPUSH, 10);
//            methodVisitor.visitVarInsn(ISTORE, 2);
//            Label label4 = new Label();
//            methodVisitor.visitLabel(label4);
//            methodVisitor.visitLineNumber(104, label4);
//            methodVisitor.visitLdcInsn("max_editor_tab_chars");
//            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
//            methodVisitor.visitVarInsn(ASTORE, 3);
//            methodVisitor.visitLabel(label0);
//            methodVisitor.visitLineNumber(106, label0);
//            methodVisitor.visitVarInsn(ALOAD, 3);
//            methodVisitor.visitJumpInsn(IFNULL, label1);
//            Label label5 = new Label();
//            methodVisitor.visitLabel(label5);
//            methodVisitor.visitLineNumber(107, label5);
//            methodVisitor.visitVarInsn(ALOAD, 3);
//            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
//            methodVisitor.visitVarInsn(ISTORE, 2);
//            methodVisitor.visitLabel(label1);
//            methodVisitor.visitLineNumber(110, label1);
//            methodVisitor.visitFrame(Opcodes.F_APPEND, 2, new Object[]{Opcodes.INTEGER, "java/lang/String"}, 0, null);
//            Label label6 = new Label();
//            methodVisitor.visitJumpInsn(GOTO, label6);
//            methodVisitor.visitLabel(label2);
//            methodVisitor.visitLineNumber(108, label2);
//            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/NumberFormatException"});
//            methodVisitor.visitVarInsn(ASTORE, 4);
//            methodVisitor.visitLabel(label6);
//            methodVisitor.visitLineNumber(112, label6);
//            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
//            methodVisitor.visitVarInsn(ILOAD, 2);
//            Label label7 = new Label();
//            methodVisitor.visitJumpInsn(IF_ICMPLE, label7);
//            Label label8 = new Label();
//            methodVisitor.visitLabel(label8);
//            methodVisitor.visitLineNumber(113, label8);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitInsn(ICONST_0);
//            methodVisitor.visitVarInsn(ILOAD, 2);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(II)Ljava/lang/String;", false);
//            methodVisitor.visitVarInsn(ASTORE, 1);
//            methodVisitor.visitLabel(label7);
//            methodVisitor.visitLineNumber(116, label7);
//            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//            methodVisitor.visitVarInsn(ALOAD, 0);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/intellij/ui/tabs/TabInfo", "orig$setText", "(Ljava/lang/String;)Lcom/intellij/ui/tabs/TabInfo;", false);
//            methodVisitor.visitInsn(ARETURN);
//            Label label9 = new Label();
//            methodVisitor.visitLabel(label9);
//            methodVisitor.visitLocalVariable("this", "Lcom/intellij/ui/tabs/TabInfo;", null, label3, label9, 0);
//            methodVisitor.visitLocalVariable("text", "Ljava/lang/String;", null, label3, label9, 1);
//            methodVisitor.visitLocalVariable("maxChars", "I", null, label4, label9, 2);
//            methodVisitor.visitLocalVariable("max_editor_tab_chars", "Ljava/lang/String;", null, label0, label9, 3);
//            methodVisitor.visitMaxs(3, 5);
//            methodVisitor.visitEnd();

            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/NumberFormatException");
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(103, label3);
            methodVisitor.visitIntInsn(BIPUSH, 10);
            methodVisitor.visitVarInsn(ISTORE, 2);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(104, label4);
            methodVisitor.visitLdcInsn("max_editor_tab_chars");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(105, label5);
            methodVisitor.visitLdcInsn("fill_with_spaces");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
            methodVisitor.visitVarInsn(ISTORE, 4);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(108, label0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitJumpInsn(IFNULL, label1);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(109, label6);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
            methodVisitor.visitVarInsn(ISTORE, 2);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(112, label1);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 3, new Object[]{Opcodes.INTEGER, "java/lang/String", Opcodes.INTEGER}, 0, null);
            Label label7 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label7);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(110, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/NumberFormatException"});
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLineNumber(114, label7);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
            methodVisitor.visitVarInsn(ILOAD, 2);
            Label label8 = new Label();
            methodVisitor.visitJumpInsn(IF_ICMPLE, label8);
            Label label9 = new Label();
            methodVisitor.visitLabel(label9);
            methodVisitor.visitLineNumber(115, label9);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ILOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(II)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label10 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label10);
            methodVisitor.visitLabel(label8);
            methodVisitor.visitLineNumber(116, label8);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ILOAD, 4);
            methodVisitor.visitJumpInsn(IFEQ, label10);
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitLineNumber(117, label11);
            methodVisitor.visitVarInsn(ILOAD, 2);
            methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants", "(I)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"%-\u0001s"});
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
            methodVisitor.visitLabel(label10);
            methodVisitor.visitLineNumber(120, label10);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/intellij/ui/tabs/TabInfo", "orig$setText", "(Ljava/lang/String;)Lcom/intellij/ui/tabs/TabInfo;", false);
            methodVisitor.visitInsn(ARETURN);
            Label label12 = new Label();
            methodVisitor.visitLabel(label12);
            methodVisitor.visitLocalVariable("this", "Lcom/intellij/ui/tabs/TabInfo;", null, label3, label12, 0);
            methodVisitor.visitLocalVariable("text", "Ljava/lang/String;", null, label3, label12, 1);
            methodVisitor.visitLocalVariable("maxChars", "I", null, label4, label12, 2);
            methodVisitor.visitLocalVariable("max_editor_tab_chars", "Ljava/lang/String;", null, label5, label12, 3);
            methodVisitor.visitLocalVariable("fillWithSpaces", "Z", null, label0, label12, 4);
            methodVisitor.visitMaxs(5, 6);
            methodVisitor.visitEnd();

        }
    }


    private void generateGetTooltipBody(int access, String desc, String signature, String[] exceptions,
                                     String name) {

        {
            final MethodVisitor methodVisitor = cv.visitMethod(ACC_PUBLIC, "getTooltipText", "()Ljava/lang/String;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(405, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/intellij/ui/tabs/TabInfo", "orig$getTooltipText", "()Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(406, label1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            Label label2 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label2);
            methodVisitor.visitLdcInsn("only_file_name_in_tooltip");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
            methodVisitor.visitJumpInsn(IFEQ, label2);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(407, label3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitIntInsn(BIPUSH, 47);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "lastIndexOf", "(I)I", false);
            methodVisitor.visitInsn(ICONST_M1);
            Label label4 = new Label();
            methodVisitor.visitJumpInsn(IF_ICMPLE, label4);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(408, label5);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitIntInsn(BIPUSH, 47);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "lastIndexOf", "(I)I", false);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitInsn(IADD);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(I)Ljava/lang/String;", false);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(411, label4);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/lang/String"}, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitIntInsn(BIPUSH, 92);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "lastIndexOf", "(I)I", false);
            methodVisitor.visitInsn(ICONST_M1);
            methodVisitor.visitJumpInsn(IF_ICMPLE, label2);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(412, label6);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitIntInsn(BIPUSH, 92);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "lastIndexOf", "(I)I", false);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitInsn(IADD);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(I)Ljava/lang/String;", false);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(415, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(ARETURN);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLocalVariable("this", "Lcom/intellij/ui/tabs/TabInfo;", null, label0, label7, 0);
            methodVisitor.visitLocalVariable("tooltipText", "Ljava/lang/String;", null, label1, label7, 1);
            methodVisitor.visitMaxs(3, 2);
            methodVisitor.visitEnd();
        }
    }
}