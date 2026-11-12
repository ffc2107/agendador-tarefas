package com.fabio.agendadortarefas.business.mapper;

import com.fabio.agendadortarefas.business.dto.TarefasDTO;
import com.fabio.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaUpdateConverter {
    void updateTarefas(TarefasDTO tarefasDTO, @MappingTarget TarefasEntity tarefasEntity);
}
