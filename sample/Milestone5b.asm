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
addi $t1, $zero, 10
slt $t2, $t0, $t1
beq $t2, $zero, L1
addi $t3, $zero, 5
add $t4, $t3, $t0
j L2
L1:
addi $t4, $zero, 0
L2:
add $v0, $zero, $t4
;epilogue
jr $ra
