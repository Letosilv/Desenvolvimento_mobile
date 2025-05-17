package com.example.hamburgueriaz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declaração das variáveis
    private int quantidade = 0;
    private TextView quantidadeView;
    private EditText nomeCliente;
    private TextView resumoPedido;
    private TextView valorTotal;
    private CheckBox baconCheckBox;
    private CheckBox queijoCheckBox;
    private CheckBox onionRingsCheckBox;

    // Preços dos itens do pedido
    private final float precoHamburguer = 20.0f;
    private final float precoBacon = 2.0f;
    private final float precoQueijo = 2.0f;
    private final float precoOnionRings = 3.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização das views
        quantidadeView = findViewById(R.id.quantidade);
        nomeCliente = findViewById(R.id.nome_cliente);
        baconCheckBox = findViewById(R.id.bacon);
        queijoCheckBox = findViewById(R.id.queijo);
        onionRingsCheckBox = findViewById(R.id.onion_rings);
        resumoPedido = findViewById(R.id.resumo_pedido);
        valorTotal = findViewById(R.id.valor_total);

        // Configuração dos listeners para os botões
        Button adicionarButton = findViewById(R.id.adicionar);
        adicionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarQuantidade();
            }
        });

        Button subtrairButton = findViewById(R.id.subtrair);
        subtrairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtrairQuantidade();
            }
        });

        Button enviarPedidoButton = findViewById(R.id.enviar_pedido);
        enviarPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });

        // Configuração dos listeners para os checkboxes
        baconCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                atualizarResumoPedido();
                atualizarPrecoTotal();
            }
        });

        queijoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                atualizarResumoPedido();
                atualizarPrecoTotal();
            }
        });

        onionRingsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                atualizarResumoPedido();
                atualizarPrecoTotal();
            }
        });
    }

    // Método para adicionar a quantidade de lanches
    private void adicionarQuantidade() {
        quantidade++;
        atualizarQuantidade(); // Incrementa a quantidade de lanches
        atualizarResumoPedido(); // Atualiza a visualização da quantidade na tela
        atualizarPrecoTotal(); // Atualiza o preço total após adicionar a quantidade
    }

    // Método para subtrair a quantidade de lanches
    private void subtrairQuantidade() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidade();
            atualizarResumoPedido();
            atualizarPrecoTotal();
        }
    }

    // Método para atualizar a quantidade na view
    private void atualizarQuantidade() {
        quantidadeView.setText(String.valueOf(quantidade));
    }

    // Método para atualizar o resumo do pedido na view
    private void atualizarResumoPedido() {
        StringBuilder pedidoBuilder = new StringBuilder();
        pedidoBuilder.append("Resumo do pedido").append("\n");
        pedidoBuilder.append("Nome do cliente: ").append(nomeCliente.getText().toString()).append("\n");
        pedidoBuilder.append("Quantidade de Lanches: ").append(quantidade).append("\n\n");

        if (baconCheckBox.isChecked()) {
            pedidoBuilder.append("Com Bacon\n");
        }
        if (queijoCheckBox.isChecked()) {
            pedidoBuilder.append("Com Queijo\n");
        }
        if (onionRingsCheckBox.isChecked()) {
            pedidoBuilder.append("Com Onion Rings\n");
        }

        resumoPedido.setText(pedidoBuilder.toString());
    }

    // Método para calcular e atualizar o preço total na view
    private void atualizarPrecoTotal() {
        float precoTotal = calcularPrecoTotal();
        valorTotal.setText("Valor Total: R$ " + precoTotal);
    }

    // Método para calcular o preço total do pedido
    private float calcularPrecoTotal() {
        float precoAdicionais = (baconCheckBox.isChecked() ? precoBacon : 0) +
                (queijoCheckBox.isChecked() ? precoQueijo : 0) +
                (onionRingsCheckBox.isChecked() ? precoOnionRings : 0);

        return (precoHamburguer + precoAdicionais) * quantidade;
    }

    // Método para enviar o pedido por e-mail
    private void enviarPedido() {

        // Verifica se o campo "Nome do cliente" está vazio
        if (nomeCliente.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite seu nome no pedido", Toast.LENGTH_SHORT).show();
            return; // Retorna sem enviar o pedido se o nome estiver vazio
        }

        // Verifica se a quantidade de lanches é zero
        if (quantidade == 0) {
            Toast.makeText(this, "Selecione a quantidade de lanches", Toast.LENGTH_SHORT).show();
            return; // Retorna sem enviar o pedido se a quantidade for zero
        }

        // Constrói a mensagem detalhada do pedido
        String nome = nomeCliente.getText().toString();
        float precoTotal = calcularPrecoTotal();

        StringBuilder pedidoBuilder = new StringBuilder();
        pedidoBuilder.append("Nome do cliente: ").append(nome).append("\n\n");
        pedidoBuilder.append("Quantidade de Lanches: ").append(quantidade).append("\n\n");
        pedidoBuilder.append("Com Bacon? ").append(baconCheckBox.isChecked() ? "Sim" : "Não").append("\n");
        pedidoBuilder.append("Com Queijo? ").append(queijoCheckBox.isChecked() ? "Sim" : "Não").append("\n");
        pedidoBuilder.append("Com Onion Rings? ").append(onionRingsCheckBox.isChecked() ? "Sim" : "Não").append("\n\n");
        pedidoBuilder.append("Valor total do pedido: R$ ").append(precoTotal);

        String pedido = pedidoBuilder.toString();

        enviarEmail(nome, pedido);
    }

    // Método para enviar e-mail
    private void enviarEmail(String nome, String pedido) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "estudanteads2025@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intent.putExtra(Intent.EXTRA_TEXT, pedido);
        try {
            startActivity(Intent.createChooser(intent, "Enviar e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Cliente de e-mail não instalado. " +
                    "Por favor, instale um aplicativo de e-mail " +
                    "para enviar seu pedido.", Toast.LENGTH_SHORT).show();
        }
    }
}