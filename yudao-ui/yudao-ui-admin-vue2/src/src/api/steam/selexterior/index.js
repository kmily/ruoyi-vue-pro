import request from '@/utils/request'

// 创建外观选择
export function createSelExterior(data) {
  return request({
    url: '/steam/sel-exterior/create',
    method: 'post',
    data: data
  })
}

// 更新外观选择
export function updateSelExterior(data) {
  return request({
    url: '/steam/sel-exterior/update',
    method: 'put',
    data: data
  })
}

// 删除外观选择
export function deleteSelExterior(id) {
  return request({
    url: '/steam/sel-exterior/delete?id=' + id,
    method: 'delete'
  })
}

// 获得外观选择
export function getSelExterior(id) {
  return request({
    url: '/steam/sel-exterior/get?id=' + id,
    method: 'get'
  })
}

// 获得外观选择分页
export function getSelExteriorPage(params) {
  return request({
    url: '/steam/sel-exterior/page',
    method: 'get',
    params
  })
}
// 导出外观选择 Excel
export function exportSelExteriorExcel(params) {
  return request({
    url: '/steam/sel-exterior/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
