package life.toodoo.api.v1.mapper;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import life.toodoo.api.domain.entity.Schedule;
import life.toodoo.api.v1.model.ScheduleDTO;

@Component
public class ScheduleMapper 
{
	private final RecurrenceMapper recurrenceMapper;
	
	public ScheduleMapper(RecurrenceMapper recurrenceMapper) {
		this.recurrenceMapper = recurrenceMapper;
	}
	
	
	public Schedule mapScheduleDTOtoSchedule(ScheduleDTO scheduleDTO)
	{
		Schedule schedule = new Schedule();

		if ( scheduleDTO == null )
			return schedule;
		
		if ( scheduleDTO.getBeginDate() != null ) 
		{
			if ( scheduleDTO.getBeginTime() != null ) {
				schedule.setBeginTimestamp(scheduleDTO.getBeginDate().atTime(scheduleDTO.getBeginTime()));			
			}
			else {
				schedule.setBeginTimestamp(scheduleDTO.getBeginDate().atStartOfDay());
			}
		}
		else {
			schedule.setBeginTimestamp(LocalDateTime.now());
		}
		
		if ( scheduleDTO.getEndDate() != null )
		{
			if ( scheduleDTO.getEndTime() != null ) {
				schedule.setEndTimestamp(scheduleDTO.getEndDate().atTime(scheduleDTO.getEndTime()));
			}
			else {
				schedule.setEndTimestamp(scheduleDTO.getEndDate().atTime(LocalTime.MAX));
			}
			
			if ( schedule.getBeginTimestamp().isAfter(schedule.getEndTimestamp()) ) {
				schedule.setEndTimestamp(scheduleDTO.getEndDate().atTime(LocalTime.MAX));
				
			}
		}
		
		return schedule;
	}

	public ScheduleDTO mapScheduleToScheduleDto(Schedule schedule) 
	{
		ScheduleDTO scheduleDTO = new ScheduleDTO();
		
		if ( schedule == null )
			return scheduleDTO;
		
		if ( schedule.getBeginTimestamp() == null ) {
			scheduleDTO.setDurationInMinutess(schedule.getDurationMins());
			return scheduleDTO;
		}
		else {
			scheduleDTO.setBeginDate(schedule.getBeginTimestamp().toLocalDate());
			scheduleDTO.setBeginTime(schedule.getBeginTimestamp().toLocalTime());
			scheduleDTO.setDurationInMinutess(schedule.getDurationMins());
			scheduleDTO.setRecurrence(recurrenceMapper.mapRecurenceToRecurrenceDto(schedule.getRecurrence()));
		}
		
		if ( schedule.getEndTimestamp() != null ) {
			scheduleDTO.setEndDate(schedule.getEndTimestamp().toLocalDate());
			scheduleDTO.setEndTime(schedule.getEndTimestamp().toLocalTime());
			scheduleDTO.setDurationInMinutess(schedule.getDurationMins());
		}
		return scheduleDTO;
	}
}
