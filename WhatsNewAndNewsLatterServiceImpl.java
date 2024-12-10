package com.janaushadhi.adminservice.serviceimpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.janaushadhi.adminservice.entity.WhatsNewAndNewsLatter;
import com.janaushadhi.adminservice.repository.WhatsNewAndNewsLatterRepository;
import com.janaushadhi.adminservice.requestpayload.WhatsNewAndNewsLatterRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllByNews;
import com.janaushadhi.adminservice.responsepayload.WhatsNewAndNewsLatterPage;
import com.janaushadhi.adminservice.responsepayload.WhatsNewAndNewsLatterResponse;
import com.janaushadhi.adminservice.service.WhatsNewAndNewsLatterService;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;

@Service
@Slf4j
public class WhatsNewAndNewsLatterServiceImpl implements WhatsNewAndNewsLatterService {

	@Autowired
	private WhatsNewAndNewsLatterRepository whatsNewAndNewsLatterRepo;

	@Override
	public Map<String, Object> addWhatsNewAndNewsLatter(WhatsNewAndNewsLatterRequestPayload requestPayload) {
		Map<String, Object> map = new HashMap<String, Object>();
		WhatsNewAndNewsLatter news = new WhatsNewAndNewsLatter();
		if (requestPayload.getId() != null && requestPayload.getId() != 0) {
			news = whatsNewAndNewsLatterRepo.findById(requestPayload.getId()).orElse(null);
			if (news != null) {
				if (requestPayload.getTitle() != null && !requestPayload.getTitle().trim().isEmpty()) {
					news.setTitle(requestPayload.getTitle());
				}
				if (requestPayload.getDiscription() != null && !requestPayload.getDiscription().trim().isEmpty()) {
					news.setDiscription(requestPayload.getDiscription());
				}
				if (requestPayload.getDocFile() != null && !requestPayload.getDocFile().trim().isEmpty()) {
					news.setDocFile(requestPayload.getDocFile());
				}
				if (requestPayload.getStatus() != null) {
					news.setStatus(requestPayload.getStatus());
				}
				if (requestPayload.getImage() != null && !requestPayload.getImage().trim().isEmpty()) {
					news.setImage(requestPayload.getImage());
				}
				news.setUpdatedDate(new Date());
				whatsNewAndNewsLatterRepo.save(news);
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
				map.put(DataConstant.RESPONSE_BODY, news);
				log.info("WatsNew And NewsLetter Data Updated Successfully",DataConstant.UPDATED_SUCCESSFULLY);
				return map;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("WatsNew And NewsLetter Record Not Found",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return map;
		}
		BeanUtils.copyProperties(requestPayload, news);
		news.setCreatedDate(new Date());
		news.setStatus(1);
		if(requestPayload.getType().equals(DataConstant.WHATS_NEW) || requestPayload.getType().equals(DataConstant.NEWS_LATTER)) {
			news.setType(requestPayload.getType());
		}else {
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.INVALID_TYPE);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("WatsNew And NewsLetter Invaild Type",DataConstant.INVALID_TYPE);

			return map;	
		}
		whatsNewAndNewsLatterRepo.save(news);
		map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
		map.put(DataConstant.MESSAGE, DataConstant.RECORD_ADDED_SUCCESSFULLY);
		map.put(DataConstant.RESPONSE_BODY, news);
		log.info("WatsNew And NewsLetter Data Added Successfully ",DataConstant.RECORD_ADDED_SUCCESSFULLY);
		return map;
	}

	@Override
	public Map<String, Object> getAllByType(GetAllByNews getAllByNews) {
		Map<String, Object> map = new HashMap<String, Object>();
		WhatsNewAndNewsLatterPage responsePage = new WhatsNewAndNewsLatterPage();
		List<WhatsNewAndNewsLatterResponse> responseList = new ArrayList<WhatsNewAndNewsLatterResponse>();
		if ( getAllByNews.getPageIndex() != null &&  getAllByNews.getPageSize() != null &&   getAllByNews.getPageSize() > 0 && getAllByNews.getType() != null
				&& !getAllByNews.getType().trim().isEmpty()) {
			//List<WhatsNewAndNewsLatter> newsList =null;
			Page<WhatsNewAndNewsLatter> newsList =null;
			Pageable pageable = PageRequest.of(getAllByNews.getPageIndex(), getAllByNews.getPageSize());
			if(getAllByNews.getSearchByTitle()!=null &&! getAllByNews.getSearchByTitle().trim().isEmpty()){
				newsList = whatsNewAndNewsLatterRepo.findAllByTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(getAllByNews.getType(),getAllByNews.getSearchByTitle(), 1,pageable);
			}
			else {
				newsList = whatsNewAndNewsLatterRepo.findAllByTypeAndStatusOrderByIdDesc(getAllByNews.getType(), 1,pageable);
			}
			if (newsList.getContent() != null) {
				int index = 0;
				for (WhatsNewAndNewsLatter news : newsList.getContent()) {
					WhatsNewAndNewsLatterResponse response = new WhatsNewAndNewsLatterResponse();
					BeanUtils.copyProperties(news, response);
					response.setCreatedDate(DateUtil.convertUtcToIst(news.getCreatedDate()));
					if (news.getUpdatedDate() != null) {
						response.setUpdatedDate(DateUtil.convertUtcToIst(news.getUpdatedDate()));
					}
					// for frontEnd team pagination
					if(getAllByNews.getPageIndex() == 0) {
						response.setSerialNo(index+1);
						index++;
						//	System.out.println("index==="+index);
					}else {
						response.setSerialNo((getAllByNews.getPageSize()*getAllByNews.getPageIndex())+(index+1));
						index++;
						//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
					}
					responseList.add(response);
				}
			
				//Pageable pageable = PageRequest.of(getAllByNews.getPageIndex(), getAllByNews.getPageSize());
//				PagedListHolder<WhatsNewAndNewsLatterResponse> page = new PagedListHolder<>(responseList);
//				Pageable pageable = PageRequest.of(getAllByNews.getPageIndex(), getAllByNews.getPageSize());
//				page.setPageSize(pageable.getPageSize()); // number of items per page
//				page.setPage(pageable.getPageNumber());
				responsePage.setWhatsNewAndNewsLatterList(responseList);
				responsePage.setPageIndex(newsList.getNumber());
				responsePage.setPageSize(newsList.getSize());
				responsePage.setTotalElement(newsList.getTotalElements());
				responsePage.setTotalPages(newsList.getTotalPages());
				responsePage.setIsLastPage(newsList.isLast());
				responsePage.setIsFirstPage(newsList.isFirst());

				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
				map.put(DataConstant.RESPONSE_BODY, responsePage);
				log.info("WatsNew And NewsLetter Record Found Successfully",DataConstant.RECORD_FOUND_MESSAGE);
				return map;

			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			map.put(DataConstant.RESPONSE_BODY, responsePage);
			log.info("WatsNew And NewsLetter Record  Not Found Successfully",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_AND_TYPE_CANT_NULL);
		map.put(DataConstant.RESPONSE_BODY, null);
		log.info(DataConstant.PAGE_SIZE_AND_INDEX_AND_TYPE_CANT_NULL);

		return map;

	}
	
	@Override
	public Map<String, Object> getWhatsNewAndNewsLatterById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		WhatsNewAndNewsLatter news = new WhatsNewAndNewsLatter();
		if (id != null && id != 0) {
			news = whatsNewAndNewsLatterRepo.findById(id).orElse(null);
			if (news != null) {
				WhatsNewAndNewsLatterResponse response = new WhatsNewAndNewsLatterResponse();
				BeanUtils.copyProperties(news, response);
				response.setCreatedDate(DateUtil.convertUtcToIst(news.getCreatedDate()));
				if (news.getUpdatedDate() != null) {
					response.setUpdatedDate(DateUtil.convertUtcToIst(news.getUpdatedDate()));
				}
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
				map.put(DataConstant.RESPONSE_BODY, response);
				log.info("getWhatsNewAndNewsLatterById Data Found Successfully ",DataConstant.RECORD_FOUND_MESSAGE);

				return map;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("getWhatsNewAndNewsLatterById Data  Not Found Successfully ",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ID_NOT_NULL_AND_ZERO);
		map.put(DataConstant.RESPONSE_BODY, null);
		log.info("getWhatsNewAndNewsLatterById Api Id Should Not Be Null OR Zero (0) ",DataConstant.ID_NOT_NULL_AND_ZERO);

		return map;
	}
}
