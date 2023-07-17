import request from '@/utils/request'

// 创建利率数据
export function createCalcInterestRateData(data) {
  return request({
    url: '/biz/calc-interest-rate-data/create',
    method: 'post',
    data: data
  })
}

// 更新利率数据
export function updateCalcInterestRateData(data) {
  return request({
    url: '/biz/calc-interest-rate-data/update',
    method: 'put',
    data: data
  })
}

// 删除利率数据
export function deleteCalcInterestRateData(id) {
  return request({
    url: '/biz/calc-interest-rate-data/delete?id=' + id,
    method: 'delete'
  })
}

// 获得利率数据
export function getCalcInterestRateData(id) {
  return request({
    url: '/biz/calc-interest-rate-data/get?id=' + id,
    method: 'get'
  })
}

// 获得利率数据分页
export function getCalcInterestRateDataPage(query) {
  return request({
    url: '/biz/calc-interest-rate-data/page',
    method: 'get',
    params: query
  })
}

// 导出利率数据 Excel
export function exportCalcInterestRateDataExcel(query) {
  return request({
    url: '/biz/calc-interest-rate-data/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
