.data
msg_m: .asciiz "Digite m: "
msg_n: .asciiz "Digite n: "
msg_mmc: .asciiz "O MMC é: "
nl: .asciiz "\n"
m: .word 0
n: .word 0

.text
.globl main

main:
    	# Imprime string
    	li $v0, 4
    	la $a0, msg_m
    	syscall

    	# Le inteiro m
    	li $v0, 5
    	syscall
    	sw $v0, m

    	# Imprime string
    	li $v0, 4
    	la $a0, msg_n
    	syscall

    	# Ler inteiro n
    	li $v0, 5
    	syscall
    	sw $v0, n

    	# Chama a rotina mmc
    	jal mmc

    	# Salva o retorno (o MMC calculado) em $t0 temporariamente
    	move $t0, $v0 

    	# imprime string de msg MMC
    	li $v0, 4
    	la $a0, msg_mmc
    	syscall
	
    	# imprime string do MMC
    	li $v0, 1
    	move $a0, $t0
    	syscall
	
    	# acaba o programa ;D
    	li $v0, 10
    	syscall

# --------------------------------------------------------------------------
# AS 4 "SESSOES" A SEGUIR SERIA CORRESPONDENTE A FUCAO MMC (DO ITEM 'B')

mmc:
    	# Prologo da funcao mmc: salva o contexto na pilha
    	addi $sp, $sp, -12
    	sw   $ra, 8($sp)  # Salva endereco de retorno
    	sw   $s0, 4($sp)  # Salva $s0
    	sw   $s1, 0($sp)  # Salva $s1

    	li $s0, 1 # $s0 = mmc = 1
    	li $s1, 2 # $s1 = d = 2

mmc_loop:
    	# carrega m e n
    	lw $t0, m # $t0 = m
    	lw $t1, n # $t1 = n

    	# $t2 = minimoMMC = 1
    	li $t2, 1
    
    	# se m!=1 continua tentando dividir
    	bne $t0, $t2, tentaDividir
    
    	# A essa ponto do codigo pelo menos m eh 1
    	# mas talvez n ainda pode ser divisivel
    
    	# se n!=1 continua tentando dividir
    	bne $t1, $t2, tentaDividir
    	
    	j fim_mmc

tentaDividir:
    	# ------------------------------------------------------------------
    	# Params
    	#   offset 8($sp) -> &m   (endereço de m)
    	#   offset 4($sp) -> &n   (endereço de n)
    	#   offset 0($sp) -> d    (valor do divisor testado)
    	# Quem chama abre espaço na pilha, empilha os 3 parâmetros, faz o
    	# "jal" e, ao retornar, desempilha o espaço que abriu.
    	# ------------------------------------------------------------------
    	addi $sp, $sp, -12   # abre espaço para 3 palavras na pilha
    	la   $t3, m
    	sw   $t3, 8($sp)      # empilha &m
    	la   $t3, n
    	sw   $t3, 4($sp)      # empilha &n
    	sw   $s1, 0($sp)      # empilha d

    	jal divide

    	addi $sp, $sp, 12     # desempilha os parâmetros
    	beqz $v0, nao_dividiu # se não dividiu ninguém, tenta outro d.

   	mul $s0, $s0, $s1 # c.c. pelo menos um deles foi dividido. Multiplicar o MMC pelo d atual.
    	j mmc_loop

nao_dividiu:
    	addi $s1, $s1, 1 # d++
    	j mmc_loop

fim_mmc:
    	# Prepara retorno da funcao
    	move $v0, $s0     # Coloca o resultado (MMC) em $v0

    	# Epilogo: restaura o contexto
    	lw   $s1, 0($sp)
    	lw   $s0, 4($sp)
    	lw   $ra, 8($sp)
    	addi $sp, $sp, 12
    	jr   $ra          # Retorna para main

# --------------------------------------------------------------------------
# AS 3 "SESSOES" A SEGUIR SERIA CORRESPONDENTE A FUNCAO DIVIDE (DO ITEM 'A')
#   1) abre o espaço: addi $sp, $sp, -16
#   2) salva o contexto:  $ra, $t0, $t1, $t2 (registradores usados aqui)
#	3) lê os parâmetros empilhados pelo chamador (agora 16 bytes "abaixo"
#	deles na pilha, por causa do espaco aberto no passo 1):
#       $a0 = &m  (24($sp))
#       $a1 = &n  (20($sp))
#		$a2 = d   (16($sp))
# saída da função, em "fim_divide":
#	1) restaura o contexto salvo
#	2) fecha: addi $sp, $sp, 16
# 	3) retorna: jr $ra
# --------------------------------------------------------------------------
divide:
    	addi $sp, $sp, -16
    	sw   $ra, 12($sp)   # salva endereço de retorno
    	sw   $t0, 8($sp)    # salva $t0
    	sw   $t1, 4($sp)    # salva $t1
    	sw   $t2, 0($sp)    # salva $t2

    	lw   $a0, 24($sp)   # $a0 = &m
    	lw   $a1, 20($sp)   # $a1 = &n
    	lw   $a2, 16($sp)   # $a2 = d

		li $v0, 0 # retorno eh zero ate que se prove o contrario
		
		# testa *m
		lw $t0, 0($a0) # $t0 = *m
		div $t0, $a2
		mfhi $t1 # resto
		
		bnez $t1, testa_n # se resto != 0, não divide
		
		mflo $t2 # quociente
		sw $t2, 0($a0) # *m = *m / d
		li $v0, 1  # retorna 1 pois dividiu
   	
testa_n:
    	lw $t0, 0($a1) # $t0 = *n
    	div $t0, $a2
    	mfhi $t1 # resto

    	bnez $t1, fim_divide # se resto != 0, termina

    	mflo $t2 # quociente
    	sw $t2, 0($a1) # *n = *n / d
    	li $v0, 1 # dividiu

fim_divide:
    	lw   $t2, 0($sp)
    	lw   $t1, 4($sp)
    	lw   $t0, 8($sp)
    	lw   $ra, 12($sp)
    	addi $sp, $sp, 16
    	jr $ra
