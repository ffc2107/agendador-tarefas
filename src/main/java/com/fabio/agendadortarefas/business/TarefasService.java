package com.fabio.agendadortarefas.business;

import com.fabio.agendadortarefas.business.dto.TarefasDTO;
import com.fabio.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.fabio.agendadortarefas.business.mapper.TarefasConverter;
import com.fabio.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.fabio.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.fabio.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.fabio.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.fabio.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO gravarTarefas(String token, TarefasDTO tarefasDTO) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        tarefasDTO.setEmailUsuario(email);
        TarefasEntity tarefasEntity = tarefaConverter.paraTarefaEntity(tarefasDTO);

        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(tarefasEntity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
//        String email = jwtUtil.extrairEmailToken(token.substring(7));
//        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);
//        return tarefaConverter.paraListaTarefasDTO(listaTarefas);
//        ou
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByEmailUsuario(email));
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, o id é inexistente " + id, e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            tarefasEntity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefasEntity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause(), e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO tarefasDTO, String id) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            tarefaUpdateConverter.updateTarefas(tarefasDTO, tarefasEntity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefasEntity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }
}
