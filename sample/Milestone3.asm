Test_main:
addi $t0, $zero, 9
add $a0, $zero, $t0
jal Test2_Start
add $t1, $zero, $v0
add $a0, $zero, $t1
jal _system_out_println
jal _system_exit
Test2_Start:
add $v0, $zero, $a0
jr $ra
