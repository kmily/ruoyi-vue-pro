import request from '@/utils/request'

// 创建报告
export function createReport(data) {
  return request({
    url: '/scan/report/create',
    method: 'post',
    data: data
  })
}

// 更新报告
export function updateReport(data) {
  return request({
    url: '/scan/report/update',
    method: 'put',
    data: data
  })
}

// 删除报告
export function deleteReport(id) {
  return request({
    url: '/scan/report/delete?id=' + id,
    method: 'delete'
  })
}

// 获得报告
export function getReport(id) {
  return request({
    url: '/scan/report/get?id=' + id,
    method: 'get'
  })
}

// 获得报告分页
export function getReportPage(query) {
  return request({
    url: '/scan/report/page',
    method: 'get',
    params: query
  })
}

// 导出报告 Excel
export function exportReportExcel(query) {
  return request({
    url: '/scan/report/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
