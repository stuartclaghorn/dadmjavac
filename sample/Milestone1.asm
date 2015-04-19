Test_main:
addi $t0, $zero, 9
add $a0, $zero, $t0
jal _system_out_println
jal _system_exit
