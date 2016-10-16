package com.xiongcen.art;

/**
 * Java并发编程艺术 4.3.1 synchroinzed关键字 代码清单4-10
 * 在Synchroinzed.class目录下使用javap -v Synchroinzed.class 命令
 * Created by xiongcen on 16/10/16.
 */
public class Synchroinzed {

    public static void main(String[] args) {
        // 对Synchroinzed Class对象进行加锁
        synchronized (Synchroinzed.class) {

        }
        // 静态同步方法 对Synchroinzed Class对象进行加锁
        m();
    }

    public static synchronized void m() {

    }
}
/**
 * Classfile /Users/xiongcen/Documents/IdeaProject/ConcurrencyProject/out/production/ConcurrencyProject/com/xiongcen/art/Synchroinzed.class
 * Last modified 2016-10-16; size 587 bytes
 * MD5 checksum 59f59ce9009223f990a3e957c82e7bda
 * Compiled from "Synchroinzed.java"
 * public class com.xiongcen.art.Synchroinzed
 * minor version: 0
 * major version: 51
 * flags: ACC_PUBLIC, ACC_SUPER
 * Constant pool:
 * #1 = Methodref          #4.#23         // java/lang/Object."<init>":()V
 * #2 = Class              #24            // com/xiongcen/art/Synchroinzed
 * #3 = Methodref          #2.#25         // com/xiongcen/art/Synchroinzed.m:()V
 * #4 = Class              #26            // java/lang/Object
 * #5 = Utf8               <init>
 * #6 = Utf8               ()V
 * #7 = Utf8               Code
 * #8 = Utf8               LineNumberTable
 * #9 = Utf8               LocalVariableTable
 * #10 = Utf8               this
 * #11 = Utf8               Lcom/xiongcen/art/Synchroinzed;
 * #12 = Utf8               main
 * #13 = Utf8               ([Ljava/lang/String;)V
 * #14 = Utf8               args
 * #15 = Utf8               [Ljava/lang/String;
 * #16 = Utf8               StackMapTable
 * #17 = Class              #15            // "[Ljava/lang/String;"
 * #18 = Class              #26            // java/lang/Object
 * #19 = Class              #27            // java/lang/Throwable
 * #20 = Utf8               m
 * #21 = Utf8               SourceFile
 * #22 = Utf8               Synchroinzed.java
 * #23 = NameAndType        #5:#6          // "<init>":()V
 * #24 = Utf8               com/xiongcen/art/Synchroinzed
 * #25 = NameAndType        #20:#6         // m:()V
 * #26 = Utf8               java/lang/Object
 * #27 = Utf8               java/lang/Throwable
 * {
 * public com.xiongcen.art.Synchroinzed();
 * descriptor: ()V
 * flags: ACC_PUBLIC
 * Code:
 * stack=1, locals=1, args_size=1
 * 0: aload_0
 * 1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 * 4: return
 * LineNumberTable:
 * line 6: 0
 * LocalVariableTable:
 * Start  Length  Slot  Name   Signature
 * 0       5     0  this   Lcom/xiongcen/art/Synchroinzed;
 * <p/>
 * public static void main(java.lang.String[]);
 * descriptor: ([Ljava/lang/String;)V
 * flags: ACC_PUBLIC, ACC_STATIC        // 方法修饰符
 * Code:
 * stack=2, locals=3, args_size=1
 * 0: ldc_w         #2                  // class com/xiongcen/art/Synchroinzed
 * 3: dup
 * 4: astore_1
 * 5: monitorenter                      // 监视器进入获取锁
 * 6: aload_1
 * 7: monitorexit                       // 监视器退出释放锁
 * 8: goto          16
 * 11: astore_2
 * 12: aload_1
 * 13: monitorexit
 * 14: aload_2
 * 15: athrow
 * 16: invokestatic  #3                  // Method m:()V
 * 19: return
 * Exception table:
 * from    to  target type
 * 6     8    11   any
 * 11    14    11   any
 * LineNumberTable:
 * line 10: 0
 * line 12: 6
 * line 14: 16
 * line 15: 19
 * LocalVariableTable:
 * Start  Length  Slot  Name   Signature
 * 0      20     0  args   [Ljava/lang/String;
 * StackMapTable: number_of_entries = 2
 * frame_type = 255 // full_frame
 * offset_delta = 11
 * locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
 * stack = [ class java/lang/Throwable ]
 * frame_type = 250 // chop
 * offset_delta = 4
 * <p/>
 * public static synchronized void m();
 * descriptor: ()V
 * flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED   // 方法修饰符
 * Code:
 * stack=0, locals=0, args_size=0
 * 0: return
 * LineNumberTable:
 * line 19: 0
 * }
 * SourceFile: "Synchroinzed.java"
 */
