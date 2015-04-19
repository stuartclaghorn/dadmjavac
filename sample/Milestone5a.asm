Test_main:
addi $t0, $zero, 0
add $a0, $zero, $t0
jal Test2_Start
add $t1, $zero, $v0
add $a0, $zero, $t1
jal _system_out_println
jal _system_exit

Test2_Start:
; prologue here -- skipping for brevity
add $t0, $zero, $a0
addi $t1, $zero, 10
slt $t2, $t0, $t1
beq $t2, $zero, L1
addi $t3, $zero, 1
add $t4, $t0, $t3
add $a0, $zero, $t4
jal Test2_Start
add $t5, $zero, $v0
addi $t6, $zero, 1
add $t5, $t5, $t6
j L2
L1:
addi $t5, $zero, 0
L2:
add $v0, $zero, $t5
; epilogue here
jr $ra
