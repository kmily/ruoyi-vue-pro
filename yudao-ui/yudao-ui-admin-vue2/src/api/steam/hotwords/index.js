import request from '@/utils/request'

// 创建热词搜索
export function createHotWords(data) {
  return request({
    url: '/steam/hot-words/create',
    method: 'post',
    data: data
  })
}

// 更新热词搜索
export function updateHotWords(data) {
  return request({
    url: '/steam/hot-words/update',
    method: 'put',
    data: data
  })
}

// 删除热词搜索
export function deleteHotWords(id) {
  return request({
    url: '/steam/hot-words/delete?id=' + id,
    method: 'delete'
  })
}

// 获得热词搜索
export function getHotWords(id) {
  return request({
    url: '/steam/hot-words/get?id=' + id,
    method: 'get'
  })
}

// 获得热词搜索分页
export function getHotWordsPage(params) {
  return request({
    url: '/steam/hot-words/page',
    method: 'get',
    params
  })
}
// 导出热词搜索 Excel
export function exportHotWordsExcel(params) {
  return request({
    url: '/steam/hot-words/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}