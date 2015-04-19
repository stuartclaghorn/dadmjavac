Test_main:
addi $t0, $zero, 0
add $a0, $zero, $t0
jal Test2_Start
add $t1, $zero, $v0
add $a0, $zero, $t1
jal _system_out_println
jal _system_exit

Test2_Start:
;prologue
add $t0, $zero, $a0
add $t1, $zero, $t0
L1:
addi $t3, $zero, 10
slt $t4, $t1, $t3
beq $t4, $zero, L2
addi $t5, $zero, 1
add $t1, $t1, $t8
j L1
L2:
add $v0, $zero, $t1
;epilogue
jr $ra
