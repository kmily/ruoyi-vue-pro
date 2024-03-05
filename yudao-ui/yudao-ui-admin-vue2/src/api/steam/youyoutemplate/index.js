import request from '@/utils/request'

// 创建悠悠商品模板
export function createYouyouTemplate(data) {
  return request({
    url: '/steam/youyou-template/create',
    method: 'post',
    data: data
  })
}

// 更新悠悠商品模板
export function updateYouyouTemplate(data) {
  return request({
    url: '/steam/youyou-template/update',
    method: 'put',
    data: data
  })
}

// 删除悠悠商品模板
export function deleteYouyouTemplate(id) {
  return request({
    url: '/steam/youyou-template/delete?id=' + id,
    method: 'delete'
  })
}

// 获得悠悠商品模板
export function getYouyouTemplate(id) {
  return request({
    url: '/steam/youyou-template/get?id=' + id,
    method: 'get'
  })
}

// 获得悠悠商品模板分页
export function getYouyouTemplatePage(params) {
  return request({
    url: '/steam/youyou-template/page',
    method: 'get',
    params
  })
}
// 导出悠悠商品模板 Excel
export function exportYouyouTemplateExcel(params) {
  return request({
    url: '/steam/youyou-template/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}