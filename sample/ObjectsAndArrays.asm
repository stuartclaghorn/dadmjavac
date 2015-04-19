Test_main:
addi $a0, $zero, 8  ; two 4-byte instance vars -- should be calculated from the number of instance vars and not hard-coded
jal _new_object
add $t0, $zero, $v0
addi $t1, $zero, 0
add $a0, $zero, $t0  ; binding object as the first arg
add $a1, $zero, $t1
jal Test2_Start
add $t2, $zero, $v0
add $a0, $zero, $t2
jal _system_out_println
jal _system_exit
 
Test2_Start:
;prologue
add $t0, $zero, $a0
add $t1, $zero, $a1
addi $t2, $zero, 2
add $t3, $t1, $t2
sw $t3, 0($t0)  ; write the new value of x to the object in memory; it's at an offset of 0 from the address in $t0
addi $t4, $zero, 7
mult $t5, $t3, $t4
sw $t5, 4($t0)  ; write the new value of z to the object in memory; it's at an offset of 4 from the address in $t1
add $t1, $zero, $t5
;epilogue
add $v0, $zero, $t1
jr $ra
