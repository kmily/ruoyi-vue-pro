import request from '@/utils/request'

// 创建下拉选择菜单
export function createClassChoose(data) {
  return request({
    url: '/steam/class-choose/create',
    method: 'post',
    data: data
  })
}

// 更新下拉选择菜单
export function updateClassChoose(data) {
  return request({
    url: '/steam/class-choose/update',
    method: 'put',
    data: data
  })
}

// 删除下拉选择菜单
export function deleteClassChoose(id) {
  return request({
    url: '/steam/class-choose/delete?id=' + id,
    method: 'delete'
  })
}

// 获得下拉选择菜单
export function getClassChoose(id) {
  return request({
    url: '/steam/class-choose/get?id=' + id,
    method: 'get'
  })
}

// 获得下拉选择菜单分页
export function getClassChoosePage(params) {
  return request({
    url: '/steam/class-choose/page',
    method: 'get',
    params
  })
}
// 导出下拉选择菜单 Excel
export function exportClassChooseExcel(params) {
  return request({
    url: '/steam/class-choose/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
