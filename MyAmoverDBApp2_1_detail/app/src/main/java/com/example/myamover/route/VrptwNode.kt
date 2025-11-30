package com.example.myamover.route


// Representa um nó da rede VRPTW (Vehicle Routing Problem with Time Windows)
// Cada nó pode ser um depósito, um cliente ou uma estação de carregamento.
data class VrptwNode(
    val id: Long,
    val x: Double, // longitude
    val y: Double, // latitude
    val is_depot: Boolean? = null,
    val is_charging_station: Boolean? = null,
    // Atributos específicos para clientes:
    val demand: Int? = null,                  // Quantidade de carga a ser entregue/recolhida neste nó
    val time_window: List<Int>? = null,       // Janela de tempo [início, fim] em segundos desde 00:00
    val service_time: Int? = null,            // Tempo de serviço no nó (em segundos)

    // Atributo específico para estação de carregamento:
    val charger_power_kw: Double? = null      // Potência do carregador (se aplicável)
)

// Representa a estrutura raiz do JSON vindo da API.
// Contém a lista de todos os nós (depósito + clientes + estações).
data class VrptwPayload(
    val nodes: List<VrptwNode>  // Lista de nós a serem usados na rota
)