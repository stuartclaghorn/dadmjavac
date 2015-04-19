Test_main:
addi $t0, $zero, 5
addi $t1, $zero, 4
add $t2, $t0, $t1
add $a0, $zero, $t2
jal _system_out_println
jal _system_exit
