import request from '@/utils/request'

// 创建其他平台模板
export function createOtherTemplate(data) {
  return request({
    url: '/steam/other-template/create',
    method: 'post',
    data: data
  })
}

// 更新其他平台模板
export function updateOtherTemplate(data) {
  return request({
    url: '/steam/other-template/update',
    method: 'put',
    data: data
  })
}

// 删除其他平台模板
export function deleteOtherTemplate(id) {
  return request({
    url: '/steam/other-template/delete?id=' + id,
    method: 'delete'
  })
}

// 获得其他平台模板
export function getOtherTemplate(id) {
  return request({
    url: '/steam/other-template/get?id=' + id,
    method: 'get'
  })
}

// 获得其他平台模板分页
export function getOtherTemplatePage(params) {
  return request({
    url: '/steam/other-template/page',
    method: 'get',
    params
  })
}
// 导出其他平台模板 Excel
export function exportOtherTemplateExcel(params) {
  return request({
    url: '/steam/other-template/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}