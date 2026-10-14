package com.fabio.agendadortarefas.business.mapper;

import com.fabio.agendadortarefas.business.dto.TarefasDTO;
import com.fabio.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefasConverter {
    TarefasEntity paraTarefaEntity(TarefasDTO tarefaDTO);

    TarefasDTO paraTarefaDTO(TarefasEntity tarefasEntity);
}
