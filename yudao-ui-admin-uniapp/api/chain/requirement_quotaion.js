import request from '@/utils/request'


// 获取用户详细信息
export function getRequirementsQuotationPage(queryParams) {
	return request({
		url: '/chain/requirements-quotation/page',
		'method': 'GET',
		'params': queryParams
	})
}


export function reportRequirementsQuotation(data) {
	return request({
		url: '/chain/requirements-quotation/report',
		'method': 'PUT',
		'data': data
	})
}


export function getRequirementsQuotationDetail(id) {
	return request({
		url: '/chain/requirements-quotation/get?id=' + id,
		'method': 'GET'
	})
}

export function createRequirementsQuotation(data) {
	return request({
		url: '/chain/requirements-quotation/create',
		'method': 'POST',
		'data': data
	})
}